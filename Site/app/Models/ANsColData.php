<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_ns_CDid
 * @property int        $A_ns_C_id
 * @property string     $A_ns_CDemail
 * @property int        $A_Cm_SitPid
 * @property string     $A_ns_CDroom
 * @property string     $A_ns_CDphone
 * @property string     $A_ns_CDfax
 * @property string     $A_ns_CDtext
 * @property string     $A_ns_CDrules
 * @property string     $A_ns_CDcontact
 * @property int        $C_Lang_id
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class ANsColData extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'A_ns_ColData';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'A_ns_CDid';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_ns_CDid', 'A_ns_C_id', 'A_ns_CDemail', 'A_Cm_SitPid', 'A_ns_CDroom', 'A_ns_CDphone', 'A_ns_CDfax', 'A_ns_CDtext', 'A_ns_CDrules', 'A_ns_CDcontact', 'C_Lang_id', 'created_at', 'updated_at', 'deleted_at'
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
        'A_ns_CDid' => 'int', 'A_ns_C_id' => 'int', 'A_ns_CDemail' => 'string', 'A_Cm_SitPid' => 'int', 'A_ns_CDroom' => 'string', 'A_ns_CDphone' => 'string', 'A_ns_CDfax' => 'string', 'A_ns_CDtext' => 'string', 'A_ns_CDrules' => 'string', 'A_ns_CDcontact' => 'string', 'C_Lang_id' => 'int', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
    ];

    /**
     * The attributes that should be mutated to dates.
     *
     * @var array
     */
    protected $dates = [
        'created_at', 'updated_at', 'deleted_at'
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
            // $article->X_User_id = auth()->user()->id;
            $article->created_at = now();
            $article->updated_at = now();
        });

        static::saving(function ($article) {
            // $article->X_User_id = auth()->user()->id;
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

    public function eq_lang()
    {
        return $this->belongsTo(CLang::class, 'C_Lang_id');
    }
    public function eq_sitp()
    {
        return $this->belongsTo(ACmSitP::class, 'A_Cm_SitPid');
    }
}
