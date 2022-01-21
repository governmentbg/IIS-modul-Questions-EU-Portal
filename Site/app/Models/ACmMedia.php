<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_Cm_Md_id
 * @property int        $A_ns_C_id
 * @property int        $A_Cm_MdT_id
 * @property Date       $A_Cm_Md_date
 * @property string     $A_Cm_Md_name
 * @property string     $A_Cm_Md_file
 * @property int        $C_Lang_id
 * @property int        $A_Cm_Md_order
 * @property int        $C_St_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class ACmMedia extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'A_Cm_Media';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_Cm_Md_id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_Cm_Md_id', 'A_Cm_Sitid', 'A_ns_C_id', 'A_Cm_MdT_id', 'A_Cm_Md_date', 'A_Cm_Md_name', 'A_Cm_Md_file', 'C_Lang_id', 'A_Cm_Md_order', 'C_St_id', 'created_at', 'updated_at', 'deleted_at'
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
        'A_Cm_Md_id' => 'int', 'A_Cm_Sitid' => 'int', 'A_ns_C_id' => 'int', 'A_Cm_MdT_id' => 'int', 'A_Cm_Md_date' => 'date', 'A_Cm_Md_name' => 'string', 'A_Cm_Md_file' => 'string', 'C_Lang_id' => 'int', 'A_Cm_Md_order' => 'int', 'C_St_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'A_Cm_Md_date', 'created_at', 'updated_at', 'deleted_at'
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

            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {

            $article->updated_at = now();
        });
    }

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_coll()
    {
        return $this->belongsTo(ANsColl::class, 'A_ns_C_id');
    }

    public function eq_sit()
    {
        return $this->belongsTo(ACmSit::class, 'A_Cm_Sitid');
    }

    public function eq_lang()
    {
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }
}
