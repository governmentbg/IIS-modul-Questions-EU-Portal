<?php

namespace App\Models;

// use App\ACmDoc;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\SoftDeletes;

/**
 * @property int        $A_Cm_DocT_id
 * @property int        $A_ns_C_id
 * @property int        $L_Act_id
 * @property string     $A_Cm_DocT_name
 * @property int        $created_at
 * @property int        $updated_at
 * @property int        $deleted_at
 */
class ACmDocType extends Model
{
    use SoftDeletes;
    /**
     * The database table used by the model.
     *
     * @var string
     */
    protected $table = 'A_Cm_Doc_Type';

    /**
     * The primary key for the model.
     *
     * @var string
     */
    protected $primaryKey = 'id';

    /**
     * Attributes that should be mass-assignable.
     *
     * @var array
     */
    protected $fillable = [
        'A_Cm_DocT_id', 'A_ns_C_id', 'L_Act_id', 'A_Cm_DocT_name', 'created_at', 'updated_at', 'deleted_at'
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
        'A_Cm_DocT_id' => 'int', 'A_ns_C_id' => 'int', 'L_Act_id' => 'int', 'A_Cm_DocT_name' => 'string', 'created_at' => 'timestamp', 'updated_at' => 'timestamp', 'deleted_at' => 'timestamp'
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

    // Scopes...

    // Functions ...

    // Relations ...
    public function eq_cm_news()
    {
        return $this->hasMany(ACmDoc::class, 'A_Cm_DocT_id');
    }
}
