<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $OP_Pr_id
 * @property Date       $OP_Pr_date
 * @property string     $OP_Pr_title
 * @property int        $OP_PrT_id
 * @property string     $OP_Pr_dv_num
 * @property Date       $OP_Pr_dv_date
 * @property string     $OP_Pr_dv_link
 * @property string     $OP_Pr_aop_an
 * @property string     $OP_Pr_aop_num
 * @property int        $OP_PrMT_id
 * @property string     $OP_Pr_body
 * @property int        $C_St_id
 * @property int        $OP_S_id
 * @property int        $X_User_id
 * @property int        $OP_Pr_E_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class OPProc extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'OP_Proc';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'OP_Pr_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'OP_Pr_id', 'OP_Pr_date', 'OP_Pr_title', 'OP_PrT_id', 'OP_Pr_dv_num', 'OP_Pr_dv_date', 'OP_Pr_dv_link', 'OP_Pr_aop_an', 'OP_Pr_aop_num', 'OP_PrMT_id', 'OP_Pr_body', 'C_St_id', 'OP_S_id', 'X_User_id', 'OP_Pr_E_id', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [];

    /**
     * The attributes that should be casted to native types.
     *
     * @var array
     */
    protected $casts = [
        'OP_Pr_id' => 'int', 'OP_Pr_date' => 'date', 'OP_Pr_title' => 'string', 'OP_PrT_id' => 'int', 'OP_Pr_dv_num' => 'string', 'OP_Pr_dv_date' => 'date', 'OP_Pr_dv_link' => 'string', 'OP_Pr_aop_an' => 'string', 'OP_Pr_aop_num' => 'string', 'OP_PrMT_id' => 'int', 'OP_Pr_body' => 'string', 'C_St_id' => 'int', 'OP_S_id' => 'int', 'X_User_id' => 'int', 'OP_Pr_E_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'OP_Pr_date', 'OP_Pr_dv_date', 'created_at', 'updated_at', 'deleted_at'
    ];

    /**
     * Indicates if the model should be timestamped.
     *
     * @var boolean
     */
    public $timestamps = false;

    public static function boot()
    {
        parent::boot();

        static::creating(function ($article) {
            $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            $article->X_User_id = auth()->user()->id;
            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...

    public function eq_article()
    {
        return $this->hasMany(OPArticles::class, 'OP_Pr_id');
    }
    public function eq_user()
    {
        return $this->belongsTo(User::class, 'id', 'X_User_id');
    }
    public function eq_e_type()
    {
        return $this->belongsTo(OPEType::class, 'OP_Pr_E_id');
    }
    public function eq_proc_type()
    {
        return $this->belongsTo(OPProcType::class, 'OP_PrT_id');
    }
    public function eq_m_type()
    {
        return $this->belongsTo(OPProcMType::class, 'OP_PrMT_id');
    }
    public function eq_status()
    {
        return $this->belongsTo(OPStatus::class, 'OP_S_id');
    }
}
